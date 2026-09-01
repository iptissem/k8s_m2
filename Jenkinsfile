pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {

        stage('Helm Lint') {
            steps {
                sh '''
                    helm lint ./test-helm
                '''
            }
        }

        stage('Deploy JobServer') {
            steps {
                sh '''
                    helm upgrade --install jobserver ./test-helm \
                      --namespace default \
                      --no-hooks
                '''
            }
        }

        stage('Check Applications') {
            steps {
                sh '''
                    kubectl rollout status deployment/applicants-api \
                      -n projet-apps \
                      --timeout=120s

                    kubectl rollout status deployment/identity-api \
                      -n projet-apps \
                      --timeout=120s

                    kubectl rollout status deployment/jobs-api \
                      -n projet-apps \
                      --timeout=120s

                    kubectl rollout status deployment/webmvc \
                      -n projet-apps \
                      --timeout=120s
                '''
            }
        }

        stage('Check Monitoring') {
            steps {
                sh '''
                    kubectl rollout status deployment/prometheus \
                      -n ns-monitoring \
                      --timeout=120s

                    kubectl rollout status deployment/grafana \
                      -n ns-monitoring \
                      --timeout=120s
                '''
            }
        }

        stage('Check Logging') {
            steps {
                sh '''
                    kubectl rollout status deployment/kibana \
                      -n ns-logging \
                      --timeout=120s

                    kubectl rollout status statefulset/elasticsearch \
                      -n ns-logging \
                      --timeout=120s

                    kubectl rollout status daemonset/fluent-bit \
                      -n ns-logging \
                      --timeout=120s
                '''
            }
        }

        stage('Check Data Services') {
            steps {
                sh '''
                    kubectl rollout status statefulset/rabbitmq \
                      -n ns-data \
                      --timeout=120s

                    kubectl rollout status statefulset/redis \
                      -n ns-data \
                      --timeout=120s

                    kubectl rollout status statefulset/sql-data \
                      -n ns-data \
                      --timeout=120s

                    kubectl rollout status statefulset/user-data \
                      -n ns-data \
                      --timeout=120s
                '''
            }
        }

        stage('Cluster Status') {
            steps {
                sh '''
                    echo "===== NODES ====="
                    kubectl get nodes -o wide

                    echo "===== APPLICATIONS ====="
                    kubectl get pods -n projet-apps -o wide

                    echo "===== DATA ====="
                    kubectl get pods -n ns-data -o wide

                    echo "===== MONITORING ====="
                    kubectl get pods -n ns-monitoring -o wide

                    echo "===== LOGGING ====="
                    kubectl get pods -n ns-logging -o wide

                    echo "===== HPA ====="
                    kubectl get hpa -n projet-apps
                '''
            }
        }
    }

    post {
        success {
            echo 'JobServer deployment and validation successful'
        }

        failure {
            echo 'JobServer deployment or validation failed'
        }
    }
}