from typing import Any

from core.plate.usecase.validate_plate_usecase import ValidatePlateUseCaseUseCase
from core.plate.gateway.plate_gateway import PlateGateway

class ValidatePlateUseCaseUseCaseImpl(ValidatePlateUseCaseUseCase):

    def execute(self, message: dict[str, Any], gateway: PlateGateway):
        analysis_report = gateway.get_analysis_report(message)
        print(analysis_report)