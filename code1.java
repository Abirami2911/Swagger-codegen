public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}

@RestController
@RequestMapping("/servers")
public class ServerController {
    private List<Server> servers = new ArrayList<>();

    @GetMapping
    public List<Server> getServers(@RequestParam(required = false) String name) {
        if (name != null) {
            return servers.stream()
                    .filter(server -> server.getName().contains(name))
                    .collect(Collectors.toList());
        } else {
            return servers;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Server> getServerById(@PathVariable String id) {
        Optional<Server> server = servers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        return server.map(s -> ResponseEntity.ok().body(s))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Server> createServer(@RequestBody Server server) {
        servers.add(server);
        return ResponseEntity.ok().body(server);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Server> updateServer(@PathVariable String id, @RequestBody Server updatedServer) {
        Optional<Server> server = servers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        if (server.isPresent()) {
            Server existingServer = server.get();
            existingServer.setName(updatedServer.getName());
            existingServer.setLanguage(updatedServer.getLanguage());
            existingServer.setFramework(updatedServer.getFramework());
            return ResponseEntity.ok().body(existingServer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable String id) {
        Optional<Server> server = servers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        if (server.isPresent()) {
            servers.remove(server.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}