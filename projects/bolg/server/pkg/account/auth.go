package account

import (
	"context"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

const (
	mdKeyToken = "x-bolg-token"
	ctxAccount = "account"
)

func Auth(db AccountDB) grpc.UnaryServerInterceptor {
	return func(
		ctx context.Context,
		req interface{},
		info *grpc.UnaryServerInfo,
		handler grpc.UnaryHandler,
	) (interface{}, error) {
		// 認証必要ないやつはアーリーリターン
		if info.FullMethod == "/bolg.BolgService/CheckHealth" {
			return handler(ctx, req)
		}

		md, ok := metadata.FromIncomingContext(ctx)
		if !ok {
			return nil, status.Error(codes.InvalidArgument, "not found metadata")
		}
		if len(md[mdKeyToken]) == 0 {
			return nil, status.Errorf(codes.InvalidArgument, "not found metadata[%s]", mdKeyToken)
		}

		// TODO: 認証処理ちゃんと実装
		tkn := md[mdKeyToken][0]
		a, err := db.Get(tkn)
		if err != nil {
			return nil, status.Error(codes.Unauthenticated, "invalid token")
		}

		newCtx := context.WithValue(ctx, ctxAccount, a)

		return handler(newCtx, req)
	}
}

func GetAccountFromCtx(ctx context.Context) (*Account, bool) {
	out, ok := ctx.Value(ctxAccount).(*Account)
	return out, ok
}
