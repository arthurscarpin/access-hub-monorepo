from pipelines import PlatePipeline
from decorators import inject_plate_pipeline


@inject_plate_pipeline
def run_pipeline(pipeline: PlatePipeline, filename: str):
    return pipeline.run(filename)

if __name__ == "__main__":
    result = run_pipeline("test.webp")
    print(result.model_dump_json(indent=2, ensure_ascii=False))