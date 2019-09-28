#!/usr/bin/env python3

"""
Copyright 2019 bolg-develpers.

This script is for moving the proto file artifacts(golang) in "bazel-bin" to the "genproto" directory.
This script expects to be executed from "make command".
"""

import sys
import os
import shutil
import glob

#bazel-bin/api/bolgchat/chat_go_grpc/api/bolgchat/room.pb.go

if len(sys.argv) != 2:
  print("usage: ./tools/genproto_move [api-name]")
  print("example: ./tools/genproto_move bolg")
  exit(1)

PROJ_NAME = sys.argv[1]

GENPROTO_DIR = "genproto/"+PROJ_NAME
if not os.path.exists(GENPROTO_DIR):
  os.mkdir(GENPROTO_DIR)

BASE_DIR = './bazel-bin/api/{}/chat_go_grpc/api'.format(PROJ_NAME)
for dir in os.listdir(BASE_DIR):
  pblist = glob.glob('{}/*.pb.go'.format(BASE_DIR+"/"+dir))
  for pb in pblist:
    shutil.move(pb, GENPROTO_DIR)

