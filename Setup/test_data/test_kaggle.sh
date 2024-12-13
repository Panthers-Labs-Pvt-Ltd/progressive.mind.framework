# generate test cases for kaggle.sh
# Valid test case - Output: The dataset has been downloaded from Kaggle.
. kaggle.sh download mllion/one-direction-all-songs-dataset
# Invalid test case - Output: 403 - Forbidden - Permission 'datasets.get' was denied.
. kaggle.sh download mllion/one-direction-all-songs-data
# Valid test case Kaggle set up - Output: The dataset has been downloaded from Kaggle.
. kaggle.sh download mllion/one-direction-all-songs-dataset
# Valid test case data folder up - Output: The dataset has been downloaded from Kaggle.
. kaggle.sh download mllion/one-direction-all-songs-dataset

#kaggle.sh upload -f file1
#kaggle.sh upload -d dataset/
