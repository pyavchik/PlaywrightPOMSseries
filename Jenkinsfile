pipeline {
    agent any

    tools {
        jdk 'JDK-11'  // Use JDK-11 or the name of your Java 11 installation in Jenkins
    }

    environment {
        SELENIUM_REMOTE_URL = 'http://66.42.83.123:4444/wd/hub'
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
                    // Try to get JDK from tool, or find Java 17 directly
                    def javaHome = tool 'JDK-11'
                    if (!javaHome || javaHome.isEmpty()) {
                        // Fallback: find Java 17 installation
                        javaHome = sh(
                            script: 'find /usr/lib/jvm -name "java-17*" -type d | head -1 || find /opt -name "java-17*" -type d | head -1 || echo ""',
                            returnStdout: true
                        ).trim()
                    }
                    if (!javaHome || javaHome.isEmpty()) {
                        error "Java 11/17 not found. Please configure JDK-11 tool in Jenkins or install Java 17."
                    }
                    withEnv(["JAVA_HOME=${javaHome}", "PATH+JDK=${javaHome}/bin"]) {
                        sh """
                            export JAVA_HOME=${javaHome}
                            export PATH=${javaHome}/bin:\$PATH
                            echo "Java Home: \$JAVA_HOME"
                            echo "Java version:"
                            java -version
                            echo "Maven Java version:"
                            mvn -version
                            echo "Installing Playwright browsers..."
                            mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium" || true
                            mvn -Dmaven.test.failure.ignore=true clean package
                        """
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
                    // Try to get JDK from tool, or find Java 17 directly
                    def javaHome = tool 'JDK-11'
                    if (!javaHome || javaHome.isEmpty()) {
                        // Fallback: find Java 17 installation
                        javaHome = sh(
                            script: 'find /usr/lib/jvm -name "java-17*" -type d | head -1 || find /opt -name "java-17*" -type d | head -1 || echo ""',
                            returnStdout: true
                        ).trim()
                    }
                    if (!javaHome || javaHome.isEmpty()) {
                        error "Java 11/17 not found. Please configure JDK-11 tool in Jenkins or install Java 17."
                    }
                    withEnv(["JAVA_HOME=${javaHome}", "PATH+JDK=${javaHome}/bin"]) {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh """
                                export JAVA_HOME=${javaHome}
                                export PATH=${javaHome}/bin:\$PATH
                                echo "Java Home: \$JAVA_HOME"
                                echo "Java version:"
                                java -version
                                echo "Installing Playwright browsers if needed..."
                                mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium" || true
                                mvn clean test -Dsurefire.suiteXmlFiles=src/test/testrunners/testng_regressions.xml
                            """
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