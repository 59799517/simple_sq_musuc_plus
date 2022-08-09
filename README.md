docker build -t sqmusicplusserver .
docker rmi sqmusicplusserver


docker run --name="sqmusicplusserver" -e UID=1000 -e GID=100  -p 8022:8083 -v /mnt/user/media/newmusic:/music  -v  /mnt/user/appdata/sqmusic:/sqmusic sqmusicplusserver