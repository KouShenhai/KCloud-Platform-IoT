
/* *
 *
 * @author whiteshader@163.com
 * @datetime  2021/09/16
 * 
 * */
declare namespace API.Monitor {

  export type CommandInfo = {
    name: string;
    value: string;
  };

  export type CacheRuntimeInfo = {
    active_defrag_hits: string;
    active_defrag_key_hits: string;
    active_defrag_key_misses: string;
    active_defrag_misses: string;
    active_defrag_running: string;
    allocator_active: string;
    allocator_allocated: string;
    allocator_frag_bytes: string;
    allocator_frag_ratio: string;
    allocator_resident: string;
    allocator_rss_bytes: string;
    allocator_rss_ratio: string;
    aof_current_rewrite_time_sec: string;
    aof_enabled: string;
    aof_last_bgrewrite_status: string;
    aof_last_cow_size: string;
    aof_last_rewrite_time_sec: string;
    aof_last_write_status: string;
    aof_rewrite_in_progress: string;
    aof_rewrite_scheduled: string;
    arch_bits: string;
    atomicvar_api: string;
    blocked_clients: string;
    client_recent_max_input_buffer: string;
    client_recent_max_output_buffer: string;
    cluster_enabled: string;
    config_file: string;
    configured_hz: string;
    connected_clients: string;
    connected_slaves: string;
    db0: string;
    db1: string;
    evicted_keys: string;
    executable: string;
    expired_keys: string;
    expired_stale_perc: string;
    expired_time_cap_reached_count: string;
    gcc_version: string;
    hz: string;
    instantaneous_input_kbps: number;
    instantaneous_ops_per_sec: string;
    instantaneous_output_kbps: number;
    keyspace_hits: string;
    keyspace_misses: string;
    latest_fork_usec: string;
    lazyfree_pending_objects: string;
    loading: string;
    lru_clock: string;
    master_repl_offset: string;
    master_replid: string;
    master_replid2: string;
    maxmemory: string;
    maxmemory_human: string;
    maxmemory_policy: string;
    mem_allocator: string;
    mem_aof_buffer: string;
    mem_clients_normal: string;
    mem_clients_slaves: string;
    mem_fragmentation_bytes: string;
    mem_fragmentation_ratio: string;
    mem_not_counted_for_evict: string;
    mem_replication_backlog: string;
    migrate_cached_sockets: string;
    multiplexing_api: string;
    number_of_cached_scripts: string;
    os: string;
    process_id: string;
    pubsub_channels: string;
    pubsub_patterns: string;
    rdb_bgsave_in_progress: string;
    rdb_changes_since_last_save: string;
    rdb_current_bgsave_time_sec: string;
    rdb_last_bgsave_status: string;
    rdb_last_bgsave_time_sec: string;
    rdb_last_cow_size: string;
    rdb_last_save_time: string;
    redis_build_id: string;
    redis_git_dirty: string;
    redis_git_sha1: string;
    redis_mode: string;
    redis_version: string;
    rejected_connections: string;
    repl_backlog_active: string;
    repl_backlog_first_byte_offset: string;
    repl_backlog_histlen: string;
    repl_backlog_size: string;
    role: string;
    rss_overhead_bytes: string;
    rss_overhead_ratio: string;
    run_id: string;
    second_repl_offset: string;
    slave_expires_tracked_keys: string;
    sync_full: string;
    sync_partial_err: string;
    sync_partial_ok: string;
    tcp_port: string;
    total_commands_processed: string;
    total_connections_received: string;
    total_net_input_bytes: string;
    total_net_output_bytes: string;
    total_system_memory: number;
    total_system_memory_human: string;
    uptime_in_days: string;
    uptime_in_seconds: string;
    used_cpu_sys: string;
    used_cpu_sys_children: string;
    used_cpu_user: string;
    used_cpu_user_children: number;
    used_memory: number;
    used_memory_dataset: number;
    used_memory_dataset_perc: string;
    used_memory_human: string;
    used_memory_lua: string;
    used_memory_lua_human: string;
    used_memory_overhead: string;
    used_memory_peak: string;
    used_memory_peak_human: string;
    used_memory_peak_perc: string;
    used_memory_rss: string;
    used_memory_rss_human: string;
    used_memory_scripts: string;
    used_memory_scripts_human: string;
    used_memory_startup: string;
  };

  export type CacheInfo = {
    commandStats: CommandInfo[];
    dbSize: number;
    info: CacheRuntimeInfo;
  };

  export type CacheInfoResult = {
    data: CacheInfo;
    code: number;
    msg: string;
  };

}