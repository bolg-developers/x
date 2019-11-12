# TODO: THIS SHOULD BE UPGRADED.
genproto-go:
	protoc -I . $(PROTO_FILE) --go_out=plugins=grpc:$(GENPROTO_DIR)

# TODO: THIS SHOULD BE UPGRADED.
genproto-java:
	protoc -I . $(PROTO_FILE) --grpc-java_out=lite:$(GENPROTO_DIR) --java_out=$(GENPROTO_DIR) --plugin=protoc-gen-grpc-java=$(PROTOC_GEN_GRPC_JAVA)
