#include <IVibrator.h>
#include "Tasks/VibratorTask.h"
#include <memory>

namespace bolg
{
    static std::unique_ptr<IVibrator> vibrator(new VibratorTask);

    IVibrator* GetVibrator()
    {
        return vibrator.get();
    }
}