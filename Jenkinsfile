pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        sh '''export GPG_TTY=$(tty);
mvn clean deploy -P release'''
      }
    }
    stage('archive') {
      steps {
        junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
      }
    }
  }
}