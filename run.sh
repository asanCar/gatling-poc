#!/usr/bin/env bash

GATLING_PATH=~/gatling-charts-highcharts-bundle-2.2.5/bin
DATA_FOLDER=`pwd`/data # Data to feed experiments
RESULTS_FOLDER=`pwd`/results # Gatling outputs
TESTS_FOLDER=`pwd` # Main simulations folder
SCENARIO=simulations.NormalUsageSimulation
EXPERIMENT="1"
URL=\""http:\/\/localhost:8080"\"

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --health)
    SCENARIO=simulations.HealthCheckSimulation
    SIM_NAME=HealthCheck
    shift # past argument
    ;;
    -e|--experiment)
    EXPERIMENT=\""$2"\"
    SIM_NAME=Experiment"$2"
    shift # past argument
    shift # past value
    ;;
    -gp|--gatling-folder)
    GATLING_PATH="$2"
    shift # past argument
    shift # past value
    ;;
    -df|--data-folder)
    DATA_FOLDER="$2"
    shift # past argument
    shift # past value
    ;;
    -rf|--result-folder)
    RESULTS_FOLDER="$2"
    shift # past argument
    shift # past value
    ;;
    -tf|--test-folder)
    TESTS_FOLDER="$2"
    shift # past argument
    shift # past value
    ;;
    -u|--url)
    URL="$2"
    URL=\"${URL//"/"/"\/"}\"
    shift # past argument
    shift # past value
    ;;
    *)
    echo "Usage: run.sh [options]"
    echo ""
    echo "Options:"
    echo "      --health                  Run HealthCheck scenario"
    echo "  -e, --experiment <number>     Select experimentation set"
    echo "  -u, --url <url>               URL to test"
    echo "  -gp, --gatling-folder <path>  Gatling installation folder"
    echo "  -df, --data-folder <path>     Folder with experiment's data"
    echo "  -rf, --result-folder <path>   Folder for Gatling output"
    echo "  -tf, --test-folder <path>     Main simulations folder"
    exit 1
    ;;
esac
done

if [[ $# -ne 1 ]]; then
   SIMULATION_FOLDER="${TESTS_FOLDER}"/simulations # Simulations scripts
   BINARIES_FOLDER="${TESTS_FOLDER}"/binaries # Compiled scripts
   sed -i -e "s/\(experiment=\).*/\1${EXPERIMENT}/" "${TESTS_FOLDER}"/simulation-properties.conf
   sed -i -e "s/\(url=\).*/\1${URL}/" "${TESTS_FOLDER}"/simulation-properties.conf
   cd "${GATLING_PATH}"
   ./gatling.sh -m  -df "${DATA_FOLDER}" -rf "${RESULTS_FOLDER}" -sf "${SIMULATION_FOLDER}" -bf "${BINARIES_FOLDER}" -s "${SCENARIO}" -on "${SIM_NAME}"
else
    echo "Usage: run.sh [options]"
    echo ""
    echo "Options:"
    echo "      --health                  Run HealthCheck scenario"
    echo "  -e, --experiment <number>     Select experimentation set"
    echo "  -u, --url <url>               URL to test"
    echo "  -gp, --gatling-folder <path>  Gatling installation folder"
    echo "  -df, --data-folder <path>     Folder with experiment's data"
    echo "  -rf, --result-folder <path>   Folder for Gatling output"
    echo "  -tf, --test-folder <path>     Main simulations folder"
fi