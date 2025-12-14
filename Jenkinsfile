pipeline {
    agent any

    tools {
        jdk 'JDK-11'  // Use JDK-11 or the name of your Java 11 installation in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/pyavchik/PlaywrightPOMSseries.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    def javaHome = tool 'JDK-11'
                    withEnv(["JAVA_HOME=${javaHome}", "PATH+JDK=${javaHome}/bin"]) {
                        sh '''
                            echo "Java version:"
                            java -version
                            echo "JAVA_HOME: $JAVA_HOME"
                            echo "Maven Java version:"
                            mvn -version
                            mvn -Dmaven.test.failure.ignore=true clean package
                        '''
                    }
                }
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Regression Automation Test') {
            steps {
                script {
                    def javaHome = tool 'JDK-11'
                    withEnv(["JAVA_HOME=${javaHome}", "PATH+JDK=${javaHome}/bin"]) {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh '''
                                echo "Java version:"
                                java -version
                                echo "JAVA_HOME: $JAVA_HOME"
                                mvn clean test -Dsurefire.suiteXmlFiles=src/test/testrunners/testng_regressions.xml
                            '''
                        }
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport_*.html',
                    reportName: 'HTML Extent Report',
                    reportTitles: ''
                ])
            }
        }
    }
}