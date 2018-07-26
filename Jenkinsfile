pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        sh 'mvn clean deploy -P release'
      }
    }
    stage('archive') {
      steps {
        junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
      }
    }
  }
}