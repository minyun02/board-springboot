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

function find_current_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

      if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
      then
          CURRENT_PROFILE=real2
      else
          CURRENT_PROFILE=$(curl -s http://localhost/profile)
      fi

      if [ ${CURRENT_PROFILE} == real1 ]
      then
        CURRENT_PROFILE=real1
      else
        CURRENT_PROFILE=real2
      fi

      echo "${CURRENT_PROFILE}"
  }

  function find_port()
  {
      IDLE_PROFILE=$(find_new_profile)

      if [ ${IDLE_PROFILE} == real1 ]
      then
        echo "8081"
      else
        echo "8082"
      fi
  }

  function find_old_port()
  {
      IDLE_PROFILE=$(find_new_profile)

      if [ ${IDLE_PROFILE} == real1 ]
      then
        echo "8082"
      else
        echo "8081"
      fi
  }
