pipeline {
    agent { label 'docker' }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = "foodiedash"
        COMPOSE_FILE = "docker-compose.yaml"
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
                command -v docker >/dev/null 2>&1 || { echo "Docker CLI is missing on this Jenkins agent."; exit 1; }
                docker --version
                docker compose version
                '''
            }
        }

        stage('Inject .env') {
            steps {
                withCredentials([file(credentialsId: 'foodiedash-env', variable: 'ENV_FILE')]) {
                    sh '''
                    echo "Injecting .env file..."
                    cp $ENV_FILE .env
                    '''
                }
            }
        }

        stage('Build images') {
            steps {
                echo "Building Docker images..."
                sh 'docker compose -f "${COMPOSE_FILE}" build backend'
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
                sleep 10
                curl -f http://localhost:8080/actuator/health || exit 1
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