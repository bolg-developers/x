package kakin

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestStamina_Use(t *testing.T) {
	const (
		userID   = "fakeuser"
		maxCount = 3
	)
	now = func() time.Time { return time.Date(2001, 5, 20, 23, 59, 59, 0, time.Local) }
	type fields struct {
		UserID       string
		Count        int64
		MaxCount     int64
		RecoveryTime time.Time
	}
	type wants struct {
		OK                bool
		Count             int64
		RecoveryTimeCheck bool
	}
	tests := []struct {
		name   string
		fields fields
		wants  wants
	}{
		{
			name: "use ng",
			fields: fields{
				UserID:   userID,
				Count:    0,
				MaxCount: maxCount,
			},
			wants: wants{
				OK:                false,
				Count:             0,
				RecoveryTimeCheck: false,
			},
		},
		{
			name: "use ok",
			fields: fields{
				UserID:   userID,
				Count:    3,
				MaxCount: maxCount,
			},
			wants: wants{
				OK:                true,
				Count:             2,
				RecoveryTimeCheck: true,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s := &Stamina{
				UserID:       tt.fields.UserID,
				Count:        tt.fields.Count,
				MaxCount:     tt.fields.MaxCount,
				RecoveryTime: tt.fields.RecoveryTime,
			}
			ok := s.Use()
			assert.Equal(t, tt.wants.OK, ok)
			assert.Equal(t, tt.wants.Count, s.Count)
			if tt.wants.RecoveryTimeCheck {
				assert.Equal(t, s.calcRecoveryTime(), s.RecoveryTime)
			}
		})
	}
}

func TestStamina_ForceRecovery(t *testing.T) {
	const (
		userID   = "fakeuser"
		maxCount = 3
	)
	type fields struct {
		UserID       string
		Count        int64
		MaxCount     int64
		RecoveryTime time.Time
	}
	type args struct {
		n int
	}
	tests := []struct {
		name      string
		fields    fields
		args      args
		wantCount int64
	}{
		{
			name: "normal recovery",
			fields: fields{
				UserID:   userID,
				Count:    1,
				MaxCount: 3,
			},
			args: args{
				n: 2,
			},
			wantCount: 3,
		},
		{
			name: "overflow recovery",
			fields: fields{
				UserID:   userID,
				Count:    2,
				MaxCount: 3,
			},
			args: args{
				n: 2,
			},
			wantCount: 3,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s := &Stamina{
				UserID:       tt.fields.UserID,
				Count:        tt.fields.Count,
				MaxCount:     tt.fields.MaxCount,
				RecoveryTime: tt.fields.RecoveryTime,
			}
			s.ForceRecovery(tt.args.n)
		})
	}
}

func TestStamina_countRecovery(t *testing.T) {
	const (
		userID   = "fakeuser"
		maxCount = 3
	)
	now = func() time.Time { return time.Date(2020, 2, 9, 15, 0, 0, 0, time.Local) }
	recoveryTime = 1 * 60 * 60 * 2
	type fields struct {
		UserID       string
		Count        int64
		MaxCount     int64
		RecoveryTime time.Time
	}
	tests := []struct {
		name     string
		fields   fields
		want     int
		wantTime time.Time
	}{
		{
			name: "before recovery time",
			fields: fields{
				UserID:       userID,
				Count:        2,
				MaxCount:     maxCount,
				RecoveryTime: time.Date(2020, 2, 9, 16, 0, 0, 0, time.Local),
			},
			want:     0,
			wantTime: time.Date(2020, 2, 9, 16, 0, 0, 0, time.Local),
		},
		{
			name: "just recovery time",
			fields: fields{
				UserID:       userID,
				Count:        2,
				MaxCount:     maxCount,
				RecoveryTime: time.Date(2020, 2, 9, 15, 0, 0, 0, time.Local),
			},
			want:     1,
			wantTime: time.Time{},
		},
		{
			name: "after recovery time(2.5 over)",
			fields: fields{
				UserID:   userID,
				Count:    2,
				MaxCount: maxCount,

				// +4h30min15sec10msec
				RecoveryTime: time.Date(2020, 2, 9, 10, 29, 45, 0, time.Local),
			},
			want:     2,
			wantTime: time.Date(2020, 2, 9, 15, 30, 15, 0, time.Local),
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s := &Stamina{
				UserID:       tt.fields.UserID,
				Count:        tt.fields.Count,
				MaxCount:     tt.fields.MaxCount,
				RecoveryTime: tt.fields.RecoveryTime,
			}
			got := s.countRecovery()
			assert.Equal(t, tt.want, got)
			assert.Equal(t, tt.wantTime, s.RecoveryTime)
		})
	}
}
