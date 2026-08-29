pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Helm Lint') {
            steps {
                sh 'helm lint ./test-helm'
            }
        }

        stage('Deploy JobServer') {
            steps {
                sh '''
                    helm upgrade --install jobserver ./test-helm \
                      --wait \
                      --timeout 5m
                '''
            }
        }

        stage('Check Deployment') {
            steps {
                sh '''
                    kubectl rollout status deployment/applicants-api -n projet-apps --timeout=120s
                    kubectl rollout status deployment/identity-api -n projet-apps --timeout=120s
                    kubectl rollout status deployment/jobs-api -n projet-apps --timeout=120s
                    kubectl rollout status deployment/webmvc -n projet-apps --timeout=120s
                '''
            }
        }

        stage('Cluster Status') {
            steps {
                sh '''
                    kubectl get nodes
                    kubectl get pods -n projet-apps -o wide
                '''
            }
        }
    }

    post {
        success {
            echo 'JobServer deployed successfully'
        }

        failure {
            echo 'JobServer deployment failed'
        }
    }
}