#!/bin/bash

#参数
project_path="/opt/laokou"
admin="admin"
gateway="gateway"
register="register"
auth="auth"

#运行
run() {
  cd ${project_path}

  echo 'remove file...'
  sudo rm -rf *.jar

  echo 'copy file...'
  sudo cp /var/lib/jenkins/workspace/laokou-yun/laokou-service/laokou-${admin}/target/${admin}.jar ${project_path}
  sudo cp /var/lib/jenkins/workspace/laokou-yun/laokou-cloud/laokou-${gateway}/target/${gateway}.jar ${project_path}
  sudo cp /var/lib/jenkins/workspace/laokou-yun/laokou-cloud/laokou-${register}/target/${register}.jar ${project_path}
  sudo cp /var/lib/jenkins/workspace/laokou-yun/laokou-service/laokou-${auth}/target/${auth}.jar ${project_path}

  echo "run ${admin}..."
  sudo sh laokou-${admin}.sh

  echo "run ${gateway}..."
  sudo sh laokou-${gateway}.sh

  echo "run ${register}..."
  sudo sh laokou-${register}.sh

  echo "run ${auth}..."
  sudo sh laokou-${auth}.sh
}

#打包
build() {
  echo "stop ${admin}..."
  admin_pid=`ps -ef|grep ${admin}.jar|grep -v grep|awk '{print $2}'`
  if [ -z ${admin_pid} ]; then
      echo "${admin} is already stopped..."
  else
       sudo kill ${admin_pid}
  fi

  echo "stop ${gateway}..."
  gateway_pid=`ps -ef|grep ${gateway}.jar|grep -v grep|awk '{print $2}'`
  if [ -z ${gateway_pid} ]; then
      echo "${gateway} is already stopped..."
  else
       sudo kill ${gateway_pid}
  fi

  echo "stop ${register}..."
  register_pid=`ps -ef|grep ${register}.jar|grep -v grep|awk '{print $2}'`
  if [ -z ${register_pid} ]; then
      echo "${register} is already stopped..."
  else
       sudo kill  ${register_pid}
  fi

  echo "stop ${auth}..."
  auth_pid=`ps -ef|grep ${auth}.jar|grep -v grep|awk '{print $2}'`
  if [ -z ${auth_pid} ]; then
      echo "${auth} is already stopped..."
  else
       sudo kill  ${auth_pid}
  fi

}

if [ $1 == "run" ]; then
    run
else
    build
fi