package com.example.vodafoneTest.service;

import java.util.Optional;

public interface DeviceInfoService {

	Optional<?> getDeviceInfo(String productId, Long tstmp);

}
