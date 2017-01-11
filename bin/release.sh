#!/bin/ksh

if [ $# != 1 ]
then
  echo "Usage: $0 <release number>"
  exit 1
fi

svn copy -m "Release $1 of application" \
  https://svn.kenai.com/svn/jcrmanager~svn/trunk \
  https://svn.kenai.com/svn/jcrmanager~svn/tags/$1

svnsync synchronize \
  https://sptjcrmanager.googlecode.com/svn/ --username rakesh.vidyadharan
