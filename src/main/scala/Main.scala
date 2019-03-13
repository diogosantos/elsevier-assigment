object Main extends App {
  require(args.length > 0, "Parameter `username` is required.")
  val username = args(0)

  val githubUserService = Components.githubUserService
  githubUserService.run(username)
}
