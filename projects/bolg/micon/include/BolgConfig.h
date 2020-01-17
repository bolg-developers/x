// ----------------------------------------------------------------------------
//
// @file BolgConfig.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

#define BOLG_DEBUG

namespace bolg
{
    // Version
    constexpr const char* BOLG_VERSION = "1.0.1";

    // Bluetooth
    constexpr const char* BOLG_BLUETOOTH_DEVICE_NAME = "MASAKO";

    // Pins
    constexpr uint8_t BOLG_TRIGGER_PIN       = 35;
    constexpr uint8_t BOLG_IR_RECEIVE_PINS[] = {34,16,17,4};
    constexpr uint8_t BOLG_IR_SEND_PIN       = 26;
    constexpr uint8_t BOLG_VIBRATION_PIN     = 2;

    // Command
    constexpr uint8_t COMMAND_START_BYTE = 0xfe;
    constexpr uint8_t COMMAND_END_BYTE   = 0xff;

    // GameData
    constexpr int32_t BOLG_INIT_USER_ID  = 1;
}