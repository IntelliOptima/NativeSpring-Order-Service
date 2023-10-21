# Build 
custom_build(
    # Name of the container image
    ref = 'order-service',
    # Command to build the container image
    command = 'gradle bootBuildImage --imageName=%EXPECTED_REF%',
    # Files to watch for changes to trigger a rebuild
    deps = [ 'build.gradle', 'src'],

    live_update = [
        # Sync changed files
        sync('/src', '/workspace/src'),
    ] 
)


# Deploy
k8s_yaml(kustomize('./k8s/resources/base'))

# Manage
k8s_resource('order-service', port_forwards=['7001'])