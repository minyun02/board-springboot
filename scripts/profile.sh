#!/usr/bin/env bash

function find_new_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

      if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
      then
          CURRENT_PROFILE=real2
      else
          CURRENT_PROFILE=$(curl -s http://localhost/profile)
      fi

      if [ ${CURRENT_PROFILE} == real1 ]
      then
        NEW_PROFILE=real2
      else
        NEW_PROFILE=real1
      fi

      echo "${NEW_PROFILE}"
  }

  function find_new_port()
  {
      IDLE_PROFILE=$(find_new_profile)

      if [ ${IDLE_PROFILE} == real1 ]
      then
        echo "8081"
      else
        echo "8082"
      fi
}

function find_old_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

      if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
      then
          CURRENT_PROFILE=real2
      else
          CURRENT_PROFILE=$(curl -s http://localhost/profile)
      fi

      if [ ${CURRENT_PROFILE} == real1 ]
      then
        OLD_PROFILE=real2
      else
        OLD_PROFILE=real1
      fi

      echo "${OLD_PROFILE}"
  }

  function find_old_port()
  {
      OLD_PROFILE=$(find_new_profile)

      if [ ${OLD_PROFILE} == real1 ]
      then
        echo "8081"
      else
        echo "8082"
      fi
  }
