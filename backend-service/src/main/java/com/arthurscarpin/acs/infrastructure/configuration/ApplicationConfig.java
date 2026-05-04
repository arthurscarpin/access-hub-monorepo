package com.arthurscarpin.acs.infrastructure.configuration;

import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.accessevent.usecase.GetAccessHistoryUseCase;
import com.arthurscarpin.acs.core.accessevent.usecase.GetAccessHistoryUseCaseImpl;
import com.arthurscarpin.acs.core.accessevent.usecase.ValidateAccessUseCase;
import com.arthurscarpin.acs.core.accessevent.usecase.ValidateAccessUseCaseImpl;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.owner.usecase.RegisterOwnerImpl;
import com.arthurscarpin.acs.core.owner.usecase.RegisterOwnerUseCase;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import com.arthurscarpin.acs.core.user.usecase.LoginUserUseCase;
import com.arthurscarpin.acs.core.user.usecase.LoginUserUseCaseImpl;
import com.arthurscarpin.acs.core.user.usecase.RegisterUserUseCase;
import com.arthurscarpin.acs.core.user.usecase.RegisterUserUseCaseImpl;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import com.arthurscarpin.acs.core.vehicle.usecase.RegisterVehicleUseCase;
import com.arthurscarpin.acs.core.vehicle.usecase.RegisterVehicleUseCaseImpl;
import com.arthurscarpin.acs.core.vehicle.usecase.UpdateVehicleStatusUseCase;
import com.arthurscarpin.acs.core.vehicle.usecase.UpdateVehicleStatusUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public RegisterVehicleUseCase registerVehicleUseCase(VehicleGateway vehicleGateway, OwnerGateway ownerGateway) {
        return new RegisterVehicleUseCaseImpl(vehicleGateway, ownerGateway);
    }

    @Bean
    public RegisterOwnerUseCase registerOwnerUseCase(OwnerGateway ownerGateway) {
        return new RegisterOwnerImpl(ownerGateway);
    }

    @Bean
    public UpdateVehicleStatusUseCase updateVehicleStatusUseCase(VehicleGateway vehicleGateway) {
        return new UpdateVehicleStatusUseCaseImpl(vehicleGateway);
    }

    @Bean
    public ValidateAccessUseCase validateAccessUseCase(VehicleGateway vehicleGateway, AccessEventGateway accessEventGateway) {
        return new ValidateAccessUseCaseImpl(vehicleGateway, accessEventGateway);
    }

    @Bean
    public GetAccessHistoryUseCase getAccessHistoryUseCase(AccessEventGateway accessEventGateway) {
        return new GetAccessHistoryUseCaseImpl(accessEventGateway);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserGateway userGateway, ScopeGateway scopeGateway) {
        return new RegisterUserUseCaseImpl(userGateway, scopeGateway);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(UserGateway userGateway, LoginGateway loginGateway, ScopeGateway scopeGateway) {
        return new LoginUserUseCaseImpl(userGateway, loginGateway, scopeGateway);
    }
}
