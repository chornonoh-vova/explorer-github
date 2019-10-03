workflow "build and test" {
  resolves = [
    "test",
  ]
  on = "push"
}

action "build" {
  uses = "actions/setup-java@1.8"
  runs = "./gradlew build"
}

action "test" {
  uses = "actions/setup-java@1.8"
  needs = ["build"]
  runs = "./gradlew test"
}
