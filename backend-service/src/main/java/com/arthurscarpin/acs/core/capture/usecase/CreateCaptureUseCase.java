package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;

import java.util.List;

public interface CreateCaptureUseCase {

    String execute(List<String> filenames, Direction direction);
}
