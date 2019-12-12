// ----------------------------------------------------------------------------
//
// @file BolgConfig.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

namespace bolg
{
    constexpr char* BOLG_BLUETOOTH_DEVICE_NAME = "BoLG_v1";

    constexpr uint8_t BOLG_TRIGGER_PIN    = 35;
    constexpr uint8_t BOLG_IR_RECEIVE_PINS[] = {34,16,17};
    constexpr uint8_t BOLG_IR_SEND_PIN    = 26;

    constexpr uint8_t COMMAND_START_BYTE = 0xfe;
    constexpr uint8_t COMMAND_END_BYTE   = 0xff;
}