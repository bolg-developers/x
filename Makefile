bolgchat/api/build:
	# for Go
	bazel build //api/bolgchat:chat_go_grpc
	./tools/genproto_move bolgchat
	# for Android
	# TODO: add commands
