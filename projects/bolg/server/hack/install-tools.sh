#!/usr/bin/env bash

# This script install tools needed by BoLG-Server.

# Create directory for storing tools.
mkdir $HOME/tools

# Download and install Go1.13.4.
wget https://dl.google.com/go/go1.13.4.linux-amd64.tar.gz
tar -C $HOME/tools -xzf go1.13.4.linux-amd64.tar.gz

# Download and install protoc.
# N/A

# Set PATH and GOROOT
echo 'export PATH=$PATH:$HOME/tools/go/bin' > $HOME/.profile
echo 'export GOROOT=$HOME/tools/go' > $HOME/.profile
source $HOME/.profile
