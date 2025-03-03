# Task Tracker

Task Tracker is a command-line application to help you manage and track your tasks.
https://roadmap.sh/projects/task-tracker

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed on your system.
- JAVA_HOME environment variable set.
- Windows operating system.

### Installation

1. Clone the repository to your local machine.
2. Navigate to the project directory.

### Building the Project

To build the project and generate the executable JAR file, run the following command:

```sh
.\build
```

### Running the Application

To execute the Task Tracker application, run the following command:

```sh
.\task-cli
```

## Usage

Once the application is running, you can use various commands to manage your tasks. Here are some examples:

- Add a new task:
    ```sh
    .\task-cli add <task-description>
    ```

- Update a task:
    ```sh
    .\task-cli update <task-id> <task-description>
    ```

- Delete a task:
    ```sh
    .\task-cli delete <task-id>
    ```

- Mark task in progress:
    ```sh
    .\task-cli mark-in-progress <task-id>
    ```

- Mark task done:
    ```sh
    .\task-cli mark-done <task-id>
    ```

- List tasks:
    ```sh
    .\task-cli list [todo|in-progress|done]
    ```
