/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "kaa/context/SimpleExecutorContext.hpp"

#include "kaa/logging/Log.hpp"
#include "kaa/utils/IThreadPool.hpp"
#include "kaa/common/exception/KaaException.hpp"

namespace kaa {

SimpleExecutorContext::SimpleExecutorContext(std::size_t lifeCycleThreadCount, std::size_t apiThreadCount, std::size_t callbackThreadCount)
    : apiThreadCount_(apiThreadCount), callbackThreadCount_(callbackThreadCount), lifeCycleThreadCount_(lifeCycleThreadCount)
{
    if (!lifeCycleThreadCount_ || !apiThreadCount_ || !callbackThreadCount_) {
        KAA_LOG_ERROR(boost::format("Failed to create executor context: lifeCycleThreadCount %u, apiThreadCount_ %u, callbackThreadCount_ %u,")
            % lifeCycleThreadCount_ % apiThreadCount_ % callbackThreadCount_);
        throw KaaException("Failed to crate executor context: bad input parameters");
    }
}

void SimpleExecutorContext::init()
{
    lifeCycleExecutor_ = createExecutor(DEFAULT_THREAD_COUNT);
    apiExecutor_ = createExecutor(DEFAULT_THREAD_COUNT);
    callbackExecutor_ = createExecutor(DEFAULT_THREAD_COUNT);
}

void SimpleExecutorContext::stop()
{
    shutdownExecutor(lifeCycleExecutor_);
    shutdownExecutor(apiExecutor_);
    shutdownExecutor(callbackExecutor_);
}

} /* namespace kaa */