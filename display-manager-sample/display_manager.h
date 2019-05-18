//
// Created by nm on 5/7/19.
//

#ifndef DISPLAY_MANAGER_SAMPLE_DISPLAY_MANAGER_H
#define DISPLAY_MANAGER_SAMPLE_DISPLAY_MANAGER_H

void start_x_server(const char *display, const char *vt);

void stop_x_server();

void sig_handler(int signo);

#endif //DISPLAY_MANAGER_SAMPLE_DISPLAY_MANAGER_H
