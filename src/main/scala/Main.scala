object Main extends App {
  require(args.length > 0, "Parameter `username` is required.")

  def username = args(0)

  githubUserService.run(username)

  def githubUserService = Components.githubUserService
}
