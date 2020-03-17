/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package college.springcloud.io.seata.server.event;

/**
 * The interface fot event bus.
 *  即使为了封装  com.google.common.eventbus.EventBus是适配器模式
 * @author zhengyangyong
 */
public interface EventBus {
    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Event event);
}
