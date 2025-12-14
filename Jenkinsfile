pipeline {
    agent any

    tools {
        jdk 'JDK-11'  // Use JDK-11 or the name of your Java 11 installation in Jenkins
    }

    environment {
        JAVA_HOME = "${tool 'JDK-11'}"
        PATH = "${tool 'JDK-11'}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/pyavchik/PlaywrightPOMSseries.git'
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version
                    echo "JAVA_HOME: $JAVA_HOME"
                    mvn -Dmaven.test.failure.ignore=true clean package
                '''
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh '''
                        echo "Java version:"
                        java -version
                        mvn clean test -Dsurefire.suiteXmlFiles=src/test/testrunners/testng_regressions.xml
                    '''
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