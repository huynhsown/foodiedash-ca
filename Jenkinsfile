pipeline {
    agent any

    environment {
        APP_NAME = "foodiedash"
    }

    triggers {
        githubPush()
    }

    stages {

        stage('Checkout latest code') {
            steps {
                echo "Pulling latest code from main branch..."
                git branch: 'main',
                    url: 'https://github.com/huynhsown/foodiedash-ca.git'
            }
        }

        stage('Inject env') {
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
                sh "docker compose build backend"
            }
        }

        stage('Deploy stack') {
            steps {
                echo "Starting containers..."
                sh "docker compose up -d --build"
            }
        }

        stage('Health check') {
            steps {
                echo "Checking backend health..."
                sh '''
                sleep 10
                curl -f http://localhost:8080 || exit 1
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
        }
    }
}