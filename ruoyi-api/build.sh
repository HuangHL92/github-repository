#!/bin/bash
docker build -t 192.172.18.242:5000/jybasic/jybasic-api .
docker push 192.172.18.242:5000/jybasic/jybasic-api