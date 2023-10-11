#!/bin/bash
PROJECT_NAME="board"
JAR_PATH="/home/ubuntu/board/build/libs/*.jar"
SCRIPT_PATH="/home/ubuntu/board/scripts"
DEPLOY_PATH=/home/ubuntu/$PROJECT_NAME/
DEPLOY_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy_err.log"
APPLICATION_LOG_PATH="/home/ubuntu/$PROJECT_NAME/application.log"
BUILD_JAR=$(ls $JAR_PATH)
JAR_NAME=$(basename $BUILD_JAR)

echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH

echo "> build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

source $SCRIPT_PATH/profile.sh

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH

NEW_PROFILE=$(find_new_profile)
echo "> 새로운 PROFILE -> ${NEW_PROFILE}" >> $DEPLOY_LOG_PATH
nohup java -jar -Dspring.profiles.active=$NEW_PROFILE $DEPLOY_JAR >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &
#nohup java -jar $DEPLOY_JAR >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

sleep 3

NEW_PORT=$(find_port)
echo "> 엔진엑스 설정 -> 전환할 port: ${NEW_PORT}" >> $DEPLOY_LOG_PATH
echo "> port 전환"  >> $DEPLOY_LOG_PATH
echo "set \$service_url http://127.0.0.1:${NEW_PORT};" | sudo tee /etc/nginx/includes/service-url
echo "> 엔진엑스 Reload" >> $DEPLOY_LOG_PATH
sudo service nginx reload

sleep 3

OLD_PROFILE=$(find_current_profile)
OLD_PORT=$(find_old_port)
OLD_PID=$(lsof -ti tcp:${OLD_PORT})
#OLD_PID=$(pgrep -f $JAR_NAME)

echo "> 이전 PROFILE -> ${OLD_PROFILE}" >> $DEPLOY_LOG_PATH
echo "> 이전 PORT -> ${OLD_PROFILE}" >> $DEPLOY_LOG_PATH
echo "> 현재 동작중인 어플리케이션 pid 체크" >> $DEPLOY_LOG_PATH
if [ -z $OLD_PID ]
then
  echo "> 현재 동작중인 어플리케이션 존재 X" >> $DEPLOY_LOG_PATH
else
  echo "> 현재 동작중인 어플리케이션 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 어플리케이션 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $OLD_PID" >> $DEPLOY_LOG_PATH
  kill -9 $OLD_PID
fi

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH