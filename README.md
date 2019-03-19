User Repositories
---
A simple command line tool to fetch GitHub users repositories. This project uses `sbt` for handling dependencies and running basic project lifecycle commands.

## Preparing for the run
Run  `sbt assembly` to generate the executable. 

## Running

ATTENTION: The environment variable `GITHUB_APP_TOKEN` must be set with a valid GitHub API Token for this tool to run.

Once the Github API Token is set, run the following command:

`./run.sh <github_username>`

Replace `<github_username>` for the desired GitHub username.
