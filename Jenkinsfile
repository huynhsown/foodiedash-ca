pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    environment {
        APP_NAME = "foodiedash"
        COMPOSE_FILE = "docker-compose.yaml"
        SERVER_PORT = "9090"
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Preflight') {
            steps {
                sh '''
                set -e
                command -v docker >/dev/null 2>&1 || { echo "Docker CLI not found"; exit 1; }
                docker --version
                docker compose version
                '''
            }
        }

        stage('Inject env') {
            steps {
                withCredentials([file(credentialsId: 'foodiedash-env', variable: 'ENV_FILE')]) {
                    sh '''
                    set -e
                    cp "${ENV_FILE}" .env
                    if grep -q '^SERVER_PORT=' .env; then
                      sed -i "s/^SERVER_PORT=.*/SERVER_PORT=${SERVER_PORT}/" .env
                    else
                      echo "SERVER_PORT=${SERVER_PORT}" >> .env
                    fi
                    '''
                }
            }
        }

        stage('Build images') {
            steps {
                sh 'docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" build backend'
            }
        }

        stage('Deploy stack') {
            steps {
                sh '''
                set -e
                docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" down --remove-orphans || true
                docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" up -d --build
                '''
            }
        }

        stage('Health check') {
            steps {
                sh '''
                set -e
                HEALTH_URL="http://localhost:${SERVER_PORT}/actuator/health"
                MAX_ATTEMPTS=36
                SLEEP_SECONDS=10

                for i in $(seq 1 ${MAX_ATTEMPTS}); do
                  if curl -fsS "${HEALTH_URL}" > /dev/null; then
                    echo "Backend is healthy."
                    exit 0
                  fi

                  echo "Waiting for backend... attempt ${i}/${MAX_ATTEMPTS}"
                  sleep ${SLEEP_SECONDS}
                done

                echo "Backend did not become healthy in time."
                exit 1
                '''
            }
        }
    }

    post {
        success {
            sh 'docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" ps'
        }

        failure {
            sh 'docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" logs --tail=200 || true'
        }
    }
}