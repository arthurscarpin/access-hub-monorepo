package com.arthurscarpin.acs.core.capture.usecase;

import java.util.List;

public interface CreateCaptureUseCase {

    String execute(List<String> filenames);
}
