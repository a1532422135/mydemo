package com.springcloud.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRule implements IRule {

    @Autowired
    private ILoadBalancer loadBalancer;
    private AtomicInteger i = new AtomicInteger(0);

    @Override
    public Server choose(Object o) {
        List<Server> serverList = loadBalancer.getAllServers();
        i.incrementAndGet();
        if (i.get() % 3 == 0) {
            return serverList.get(0);
        } else {
            return serverList.get(1);
        }
    }

    @Override
    public void setLoadBalancer(ILoadBalancer iLoadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return this.loadBalancer;
    }
}
