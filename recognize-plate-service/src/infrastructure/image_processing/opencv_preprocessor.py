import cv2
import numpy as np


class OpenCVPreProcessor:

    def execute(self, path: str) -> np.ndarray:
        image = cv2.imread(path)

        if image is None:
            raise ValueError(f"Não foi possível carregar a imagem: {path}")

        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        edges = cv2.Canny( cv2.bilateralFilter(gray, 11, 17, 17), 50, 150)
        contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        plate_crop = image

        for c in sorted(contours, key=cv2.contourArea, reverse=True)[:15]:
            approx = cv2.approxPolyDP(c, 0.02 * cv2.arcLength(c, True), True)

            if len(approx) == 4:
                x, y, w, h = cv2.boundingRect(approx)
                aspect_ratio = w / float(h)

                if 2.2 <= aspect_ratio <= 5.5 and w > 80 and h > 25:
                    plate_crop = image[y:y + h, x:x + w]
                    break

        processed = cv2.cvtColor(plate_crop, cv2.COLOR_BGR2GRAY)
        processed = cv2.resize(processed, None, fx=2, fy=2, interpolation=cv2.INTER_CUBIC)
        processed = cv2.bilateralFilter(processed, 9, 75, 75)
        return cv2.createCLAHE(2.0, (8, 8)).apply(processed)
