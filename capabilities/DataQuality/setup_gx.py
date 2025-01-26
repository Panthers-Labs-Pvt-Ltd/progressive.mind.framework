import great_expectations as gx
from great_expectations.data_context.data_context.file_data_context import FileDataContext

# Create a new context for the team
def create_gx_context(context_name: str) -> FileDataContext:
    context = gx.get_context(mode="file", project_root_dir="./workspace/" + context_name)
    # context.add_store(name="expectations_store", store_type="local", class_name="ExpectationsStore")
    # context.add_store(name="validations_store", store_type="local", class_name="ValidationsStore")
    # context.add_store(name="checkpoint_store", store_type="local", class_name="CheckpointStore")
    # context.add_store(name="config_variables_store", store_type="local", class_name="ConfigVariablesStore")
    # context.add_store(name="datasource_store", store_type="local", class_name="DatasourceStore")
    return context


def set_user_groups(context: FileDataContext, user_groups: list):
    context.set_config("config_variables", {"user_groups": user_groups})
   # context.set_config_variable("user_groups", user_groups)

if __name__ == "__main__":
    team_name = "team1"
    gx_context = create_gx_context(team_name)
    print(type(gx_context).__name__)
    set_user_groups(gx_context, ["data_engineers", "data_scientists"])