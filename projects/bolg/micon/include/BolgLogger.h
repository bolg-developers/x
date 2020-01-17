#include <Arduino.h>
#include <BolgConfig.h>


#if defined (BOLG_DEBUG)

#define BOLG_LOG(...)  Serial.printf(__VA_ARGS__)

#else

#define BOLG_LOG(...)

#endif