#!/bin/bash

DATA_DIR=~/data
EXTERNAL_DATA_DIR=~/data/external
WORKING_DIR=$(pwd)

# Verify Kaggle setup
verify_kaggle_setup() {
    if ! command -v kaggle &> /dev/null; then
        echo "Error: The Kaggle CLI is not installed."
        conda activate base
        pip install kaggle
        echo "Kaggle installed in base Python environment"
    fi

    if [ ! -f ~/.kaggle/kaggle.json ]; then
        echo "Error: The Kaggle API key is missing."
        if [ ! -d ~/.kaggle ]; then
            mkdir ~/.kaggle
        fi
        cd ~/.kaggle && echo '{"username":"progressivemind","key":"a3232c56dc240ca6585000d93a99adf0"}' >> kaggle.json
        chmod 600 kaggle.json
        cd "$WORKING_DIR" || exit # in case cd fails
    fi
}

# Verify data folder
verify_data_folder() {
    if [ ! -d $DATA_DIR ]; then
        mkdir $DATA_DIR
    fi
    if [ ! -d $EXTERNAL_DATA_DIR ]; then
        cd $DATA_DIR && mkdir external
    fi
    cd "$WORKING_DIR" || exit # in case cd fails
}

# Function to upload a file to Kaggle. Takes dataset name and dataset as input
# TODO: Testing
upload_file() {
    local file_type=$1
    local file=$2

    if [[ -z $file_type || -z $file ]]; then
        echo "Usage: upload_to_kaggle <file_type> <file>"
        return 1
    fi

    # file_type can be -f or -d only
    if [ "$file_type" != "-f" ] && [ "$file_type" != "-d" ]; then
        echo "Error: The file type must be -f (for file) or -d (directory)." >&2
        exit 1
    fi

    # Check if the file or directory exists
    if [ "$file_type" = "-f" ] && [ ! -f $DATA_DIR/"$file" ]; then
        echo "Error: The file $file does not exist in $DATA_DIR." >&2
        exit 1
    elif [ "$file_type" = "-d" ] && [ ! -d $DATA_DIR/"$file" ]; then
        echo "Error: The directory $file does not exist in $DATA_DIR." >&2
        exit 1
    fi

    if [ "$file_type" = "-f" ] && [ "$(stat -c %s $DATA_DIR/"$file")" -gt 1000000000 ]; then
        echo "The dataset is too large. Zipping it..."
        zip -r $DATA_DIR/"$file.zip" $DATA_DIR/"$file"
        rm $DATA_DIR/"$file"
        file=$file.zip
    fi

    if [ "$file_type" = "-d" ]; then
        echo "Zipping the directory..."
        zip -r "${DATA_DIR/$file.zip}" "${DATA_DIR/$file}"
        rm -rf "${DATA_DIR/$file}"
        file=$file.zip
    fi

    # Check if the dataset already exists on Kaggle
    if kaggle datasets list --search "$file" | grep -q "$file"; then
        echo "The dataset $file already exists on Kaggle."
        # Initialize metadata file for dataset version
        kaggle datasets init -p $DATA_DIR/"$file"
        kaggle datasets version -p $DATA_DIR/"$file" -m "Uploading dataset $file"
        echo "The dataset has been uploaded to Kaggle."
    else
        # Initialize metadata file for dataset version
        kaggle datasets init -p $DATA_DIR/"$file"

        # Upload the file to Kaggle
        kaggle datasets create -p $DATA_DIR/"$file" -m "Uploading dataset $file"
        echo "The dataset has been uploaded to Kaggle."
    fi
}

# Function to download a dataset from Kaggle. Takes dataset name as input
download_file() {
    local dataset_name=$1

    if [ -z "$dataset_name" ]; then
        echo "Usage: download_from_kaggle <dataset_name>"
        return 1
    fi

    # Check if the dataset exists on Kaggle
    if kaggle datasets list --search "$dataset_name" | grep -q "$dataset_name"; then
        # Download the dataset from Kaggle
        kaggle datasets download -d "$dataset_name" -p $EXTERNAL_DATA_DIR
        echo "The dataset has been downloaded from Kaggle."
    else
        echo "Error: The dataset $dataset_name does not exist on Kaggle." >&2
        exit 1
    fi
}

# Function to display help for this shell script
display_help() {
    echo "Usage: test_kaggle.sh [upload|download] <dataset_name> <file>"
    echo "Upload or download a dataset from Kaggle."
}

# Main function
main() {
    verify_kaggle_setup
    verify_data_folder
    if [ "$1" = "upload" ]; then
        upload_file "$2" "$3"
    elif [ "$1" = "download" ]; then
        download_file "$2"
    else
        display_help
    fi
}

main "$@"
