#!/bin/bash
echo "publish----------"

process_id=`ps -ef | grep wiki.jar | grep -v grep |awk '{print $2}'`
if [ $process_id ] ; then
sudo kill -9 $process_id
fi

source /etc/profile
/usr/bin/su - wikidev -c "java -jar -Dspring.profiles.active=prod /www/wikidev/wiki.jar > /dev/null 2>&1 &"

echo "end publish"