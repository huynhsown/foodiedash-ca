pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
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

        stage('Inject env') {
            steps {
                withCredentials([file(credentialsId: 'foodiedash-env', variable: 'ENV_FILE')]) {
                    sh '''
                    set -e
                    echo "Injecting .env file..."
                    cp $ENV_FILE .env
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
                echo "Building Docker images..."
                sh 'docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" build backend'
            }
        }

        stage('Deploy stack') {
            steps {
                echo "Starting containers..."
                sh '''
                docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" down --remove-orphans || true
                docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" up -d --build
                '''
            }
        }

        stage('Health check') {
            steps {
                echo "Checking backend health..."
                sh '''
                set -e
                sleep 10
                curl -f "http://localhost:${SERVER_PORT}/actuator/health" || exit 1
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Deployment SUCCESS!"
        }

        failure {
            echo "❌ Deployment FAILED!"
            sh 'docker compose -p "${APP_NAME}" -f "${COMPOSE_FILE}" logs --tail=200 || true'
        }
    }
}