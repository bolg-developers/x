// Code generated by protoc-gen-go. DO NOT EDIT.
// source: api/bolgchat/chat.proto

package bolgchat

import (
	context "context"
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	timestamp "github.com/golang/protobuf/ptypes/timestamp"
	grpc "google.golang.org/grpc"
	math "math"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

type SendMessageRequest struct {
	Message              string   `protobuf:"bytes,1,opt,name=message,proto3" json:"message,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *SendMessageRequest) Reset()         { *m = SendMessageRequest{} }
func (m *SendMessageRequest) String() string { return proto.CompactTextString(m) }
func (*SendMessageRequest) ProtoMessage()    {}
func (*SendMessageRequest) Descriptor() ([]byte, []int) {
	return fileDescriptor_ab8ed409f377c457, []int{0}
}

func (m *SendMessageRequest) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SendMessageRequest.Unmarshal(m, b)
}
func (m *SendMessageRequest) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SendMessageRequest.Marshal(b, m, deterministic)
}
func (m *SendMessageRequest) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SendMessageRequest.Merge(m, src)
}
func (m *SendMessageRequest) XXX_Size() int {
	return xxx_messageInfo_SendMessageRequest.Size(m)
}
func (m *SendMessageRequest) XXX_DiscardUnknown() {
	xxx_messageInfo_SendMessageRequest.DiscardUnknown(m)
}

var xxx_messageInfo_SendMessageRequest proto.InternalMessageInfo

func (m *SendMessageRequest) GetMessage() string {
	if m != nil {
		return m.Message
	}
	return ""
}

type SendMessageResponse struct {
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *SendMessageResponse) Reset()         { *m = SendMessageResponse{} }
func (m *SendMessageResponse) String() string { return proto.CompactTextString(m) }
func (*SendMessageResponse) ProtoMessage()    {}
func (*SendMessageResponse) Descriptor() ([]byte, []int) {
	return fileDescriptor_ab8ed409f377c457, []int{1}
}

func (m *SendMessageResponse) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SendMessageResponse.Unmarshal(m, b)
}
func (m *SendMessageResponse) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SendMessageResponse.Marshal(b, m, deterministic)
}
func (m *SendMessageResponse) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SendMessageResponse.Merge(m, src)
}
func (m *SendMessageResponse) XXX_Size() int {
	return xxx_messageInfo_SendMessageResponse.Size(m)
}
func (m *SendMessageResponse) XXX_DiscardUnknown() {
	xxx_messageInfo_SendMessageResponse.DiscardUnknown(m)
}

var xxx_messageInfo_SendMessageResponse proto.InternalMessageInfo

type SendMessageResponseX struct {
	PersonId             int64                `protobuf:"varint,1,opt,name=person_id,json=personId,proto3" json:"person_id,omitempty"`
	Message              string               `protobuf:"bytes,2,opt,name=message,proto3" json:"message,omitempty"`
	CreateTime           *timestamp.Timestamp `protobuf:"bytes,3,opt,name=create_time,json=createTime,proto3" json:"create_time,omitempty"`
	XXX_NoUnkeyedLiteral struct{}             `json:"-"`
	XXX_unrecognized     []byte               `json:"-"`
	XXX_sizecache        int32                `json:"-"`
}

func (m *SendMessageResponseX) Reset()         { *m = SendMessageResponseX{} }
func (m *SendMessageResponseX) String() string { return proto.CompactTextString(m) }
func (*SendMessageResponseX) ProtoMessage()    {}
func (*SendMessageResponseX) Descriptor() ([]byte, []int) {
	return fileDescriptor_ab8ed409f377c457, []int{2}
}

func (m *SendMessageResponseX) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_SendMessageResponseX.Unmarshal(m, b)
}
func (m *SendMessageResponseX) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_SendMessageResponseX.Marshal(b, m, deterministic)
}
func (m *SendMessageResponseX) XXX_Merge(src proto.Message) {
	xxx_messageInfo_SendMessageResponseX.Merge(m, src)
}
func (m *SendMessageResponseX) XXX_Size() int {
	return xxx_messageInfo_SendMessageResponseX.Size(m)
}
func (m *SendMessageResponseX) XXX_DiscardUnknown() {
	xxx_messageInfo_SendMessageResponseX.DiscardUnknown(m)
}

var xxx_messageInfo_SendMessageResponseX proto.InternalMessageInfo

func (m *SendMessageResponseX) GetPersonId() int64 {
	if m != nil {
		return m.PersonId
	}
	return 0
}

func (m *SendMessageResponseX) GetMessage() string {
	if m != nil {
		return m.Message
	}
	return ""
}

func (m *SendMessageResponseX) GetCreateTime() *timestamp.Timestamp {
	if m != nil {
		return m.CreateTime
	}
	return nil
}

func init() {
	proto.RegisterType((*SendMessageRequest)(nil), "bolgchat.SendMessageRequest")
	proto.RegisterType((*SendMessageResponse)(nil), "bolgchat.SendMessageResponse")
	proto.RegisterType((*SendMessageResponseX)(nil), "bolgchat.SendMessageResponseX")
}

func init() { proto.RegisterFile("api/bolgchat/chat.proto", fileDescriptor_ab8ed409f377c457) }

var fileDescriptor_ab8ed409f377c457 = []byte{
	// 295 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x74, 0x91, 0xc1, 0x4f, 0x83, 0x30,
	0x14, 0xc6, 0x83, 0x4b, 0x74, 0x2b, 0xb7, 0xaa, 0x71, 0x41, 0xcd, 0x16, 0x4e, 0xbb, 0xd8, 0x66,
	0xf3, 0xe6, 0x6e, 0xf3, 0xa4, 0x89, 0xc9, 0xc2, 0x3c, 0xa8, 0x17, 0x52, 0xe0, 0x59, 0x30, 0x40,
	0x91, 0x16, 0xe2, 0xdf, 0xe0, 0x5f, 0x6d, 0xda, 0xa6, 0x61, 0x8b, 0x7a, 0x21, 0xf4, 0x7b, 0xdf,
	0xeb, 0xfb, 0x7e, 0x7d, 0xe8, 0x82, 0x35, 0x05, 0x4d, 0x44, 0xc9, 0xd3, 0x9c, 0x29, 0xaa, 0x3f,
	0xa4, 0x69, 0x85, 0x12, 0x78, 0xec, 0xc4, 0x60, 0xc6, 0x85, 0xe0, 0x25, 0x50, 0xa3, 0x27, 0xdd,
	0x3b, 0x55, 0x45, 0x05, 0x52, 0xb1, 0xaa, 0xb1, 0xd6, 0x90, 0x20, 0xbc, 0x83, 0x3a, 0x7b, 0x02,
	0x29, 0x19, 0x87, 0x08, 0x3e, 0x3b, 0x90, 0x0a, 0x4f, 0xd1, 0x49, 0x65, 0x95, 0xa9, 0x37, 0xf7,
	0x16, 0x93, 0xc8, 0x1d, 0xc3, 0x73, 0x74, 0x7a, 0xe0, 0x97, 0x8d, 0xa8, 0x25, 0x84, 0xdf, 0x1e,
	0x3a, 0xfb, 0x43, 0x7f, 0xc1, 0x97, 0x68, 0xd2, 0x40, 0x2b, 0x45, 0x1d, 0x17, 0x99, 0xb9, 0x6b,
	0x14, 0x8d, 0xad, 0xf0, 0x90, 0xed, 0x8f, 0x39, 0x3a, 0x18, 0x83, 0xd7, 0xc8, 0x4f, 0x5b, 0x60,
	0x0a, 0x62, 0x1d, 0x78, 0x3a, 0x9a, 0x7b, 0x0b, 0x7f, 0x15, 0x10, 0x4b, 0x43, 0x1c, 0x0d, 0x79,
	0x76, 0x34, 0x11, 0xb2, 0x76, 0x2d, 0xac, 0x5e, 0x91, 0x7f, 0x9f, 0x33, 0xb5, 0x83, 0xb6, 0x2f,
	0x52, 0xc0, 0x8f, 0xc8, 0xdf, 0x8b, 0x86, 0xaf, 0x88, 0x7b, 0x1d, 0xf2, 0x9b, 0x3c, 0xb8, 0xfe,
	0xa7, 0x6a, 0x79, 0x36, 0x1f, 0x68, 0x26, 0x5a, 0x6e, 0x3c, 0x71, 0x06, 0x3d, 0x94, 0x42, 0xc3,
	0x0c, 0x3d, 0xfd, 0x72, 0x33, 0xd1, 0xb3, 0xb7, 0x3a, 0xe1, 0xd6, 0x7b, 0xbb, 0xe3, 0x85, 0xca,
	0xbb, 0x84, 0xa4, 0xa2, 0x32, 0x9b, 0xba, 0x19, 0x9a, 0xe8, 0x17, 0xe5, 0x50, 0x1b, 0x94, 0x61,
	0x89, 0xfd, 0x72, 0xed, 0xfe, 0x93, 0x63, 0x53, 0xbb, 0xfd, 0x09, 0x00, 0x00, 0xff, 0xff, 0xef,
	0x11, 0x68, 0xfa, 0xe7, 0x01, 0x00, 0x00,
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// ChatServiceClient is the client API for ChatService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://godoc.org/google.golang.org/grpc#ClientConn.NewStream.
type ChatServiceClient interface {
	SendMessage(ctx context.Context, in *SendMessageRequest, opts ...grpc.CallOption) (*SendMessageResponse, error)
}

type chatServiceClient struct {
	cc *grpc.ClientConn
}

func NewChatServiceClient(cc *grpc.ClientConn) ChatServiceClient {
	return &chatServiceClient{cc}
}

func (c *chatServiceClient) SendMessage(ctx context.Context, in *SendMessageRequest, opts ...grpc.CallOption) (*SendMessageResponse, error) {
	out := new(SendMessageResponse)
	err := c.cc.Invoke(ctx, "/bolgchat.ChatService/SendMessage", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// ChatServiceServer is the server API for ChatService service.
type ChatServiceServer interface {
	SendMessage(context.Context, *SendMessageRequest) (*SendMessageResponse, error)
}

func RegisterChatServiceServer(s *grpc.Server, srv ChatServiceServer) {
	s.RegisterService(&_ChatService_serviceDesc, srv)
}

func _ChatService_SendMessage_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(SendMessageRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ChatServiceServer).SendMessage(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/bolgchat.ChatService/SendMessage",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ChatServiceServer).SendMessage(ctx, req.(*SendMessageRequest))
	}
	return interceptor(ctx, in, info, handler)
}

var _ChatService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "bolgchat.ChatService",
	HandlerType: (*ChatServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "SendMessage",
			Handler:    _ChatService_SendMessage_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "api/bolgchat/chat.proto",
}
