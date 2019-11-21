#!/usr/bin/env bash

GENPROTO_DIR=genproto
PROTO_DIR=server/pb

GO_GENPROTO_DIR=server/pb

PROTOC_GEN_GRPC_JAVA=/usr/local/bin/protoc-gen-grpc-java
JAVA_GENPROTO_DIR=android/Bolg/app/src/main/java
JAVA_PACKAGE_TOP=org

if [ -e $GENPROTO_DIR ]; then
  rm -rf $GENPROTO_DIR
fi
mkdir $GENPROTO_DIR

# protoからGoとJavaのコードを生成
for file in `\find $PROTO_DIR -name '*.proto'`; do
  make genproto-go PROTO_FILE=$file GENPROTO_DIR=$GENPROTO_DIR
  make genproto-java PROTO_FILE=$file GENPROTO_DIR=$GENPROTO_DIR PROTOC_GEN_GRPC_JAVA=$PROTOC_GEN_GRPC_JAVA
done

# 生成されたGoのコードをコピー
for file in `\find $GENPROTO_DIR -name '*.pb.go'`; do
  cp -r $file $GO_GENPROTO_DIR
done

# 生成されたJavaのコードをコピー
rm -rf $JAVA_GENPROTO_DIR/$JAVA_PACKAGE_TOP
cp -r $GENPROTO_DIR/$JAVA_PACKAGE_TOP $JAVA_GENPROTO_DIR

rm -rf $GENPROTO_DIR
