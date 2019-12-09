#include "GameManager.h"

using namespace bolg;

GameManager* gameManager;

// Program SetUp
void setup()
{
    gameManager = new GameManager();
}

// Program MainLoop
void loop()
{
    gameManager->update();
    delay(100);
}