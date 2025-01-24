import great_expectations as gx

# Create a new context for the team
def create_gx_context():
    context = gx.get_context()
    context.add_store(name="expectations_store", store_type="local", class_name="ExpectationsStore")
    context.add_store(name="validations_store", store_type="local", class_name="ValidationsStore")
    context.add_store(name="checkpoint_store", store_type="local", class_name="CheckpointStore")
    context.add_store(name="config_variables_store", store_type="local", class_name="ConfigVariablesStore")
    context.add_store(name="datasource_store", store_type="local", class_name="DatasourceStore")


context = gx.get_context(mode="file", project_root_dir="./test")

print(type(context).__name__)