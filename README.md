# Saint Denis

![pipeline badge](https://szofttech.inf.elte.hu/software-technology-2023/group-1/saint-denis/badges/master/pipeline.svg)
![coverage badge](https://szofttech.inf.elte.hu/software-technology-2023/group-1/saint-denis/badges/master/coverage.svg)


# City Simulation Project

This project is a city simulation program that allows users to manage and simulate a virtual city. It provides features such as constructing buildings, managing zones, and monitoring citizen satisfaction.

## Features

- Construction of different types of buildings such as residential, industrial, services, and more.
- Management of zones to allocate buildings and control their effects.
- Monitoring and management of citizen satisfaction and workplace assignments.
- Simulation of various factors including industrial effects, bonuses, and population dynamics.

## Installation

1. Clone the repository: `git clone https://szofttech.inf.elte.hu/software-technology-2023/group-1/saint-denis.git`
2. Navigate to the project directory: `cd Saint-Denis`

## Usage

1. Compile the project: `javac Main.java`
2. Run the simulation: `java Main`

## Code Structure

The codebase is organized into the following packages:

- `settings`: Contains general settings and configurations for the simulation.
- `types`: Includes classes representing different types of objects used in the simulation, such as buildings, zones, and citizens.
- `types.Buildings`: Contains specific building classes derived from the `Building` class.
- `types.Zones`: Contains specific zone classes derived from the `Zone` class.
- `tests`: Includes JUnit tests to ensure the correctness of the implementation.

## Contributing

Contributions to this project are welcome. If you find any issues or have ideas for improvements, please open an issue or submit a pull request.

## Support
- Amal Trigui: [y19lhc@inf.elte.hu](mailto:y19lhc@inf.elte.hu)
- Latifa Allahverdiyeva: [t4ivqm@inf.elte.hu](mailto:t4ivqm@inf.elte.hu)
- Aldeken Nur: [igf8zg@inf.elte.hu](mailto:igf8zg@inf.elte.hu)
- Sarun Kumar: [iwcyyd@inf.elte.hu](mailto:iwcyyd@inf.elte.hu)


## Roadmap
- [x] General
- [x] Persistence
- [X] Disaster
- [X] Forest
- [ ] Fire department (partial)
- [ ] Demolish without notice

## Project status
In progress
